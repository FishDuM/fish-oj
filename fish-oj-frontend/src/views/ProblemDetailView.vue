<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import * as monaco from 'monaco-editor'
import { PlayCircleOutlined, SendOutlined, CodeOutlined } from '@ant-design/icons-vue'
import { useProblemStore, type ProblemDetailVO } from '@/stores/problem'
import { useJudgeStore } from '@/stores/judge'
import { useAuthStore } from '@/stores/auth'
import DifficultyBadge from '@/components/DifficultyBadge.vue'
import StatusPill from '@/components/StatusPill.vue'
import { useRouter } from 'vue-router'

// Monaco workers（Vite 注入）
self.MonacoEnvironment = {
  getWorker(_: string, label: string) {
    if (label === 'json') return new Worker(new URL('monaco-editor/esm/vs/language/json/json.worker.js', import.meta.url))
    if (label === 'css' || label === 'scss' || label === 'less') return new Worker(new URL('monaco-editor/esm/vs/language/css/css.worker.js', import.meta.url))
    if (label === 'html' || label === 'handlebars' || label === 'razor') return new Worker(new URL('monaco-editor/esm/vs/language/html/html.worker.js', import.meta.url))
    if (label === 'typescript' || label === 'javascript') return new Worker(new URL('monaco-editor/esm/vs/language/typescript/ts.worker.js', import.meta.url))
    return new Worker(new URL('monaco-editor/esm/vs/editor/editor.worker.js', import.meta.url))
  },
}

const route = useRoute()
const router = useRouter()
const problemStore = useProblemStore()
const judgeStore = useJudgeStore()
const auth = useAuthStore()

const problem = ref<ProblemDetailVO | null>(null)
const language = ref<'java' | 'cpp' | 'python'>('java')
const code = ref('')
const editorRef = ref<HTMLElement | null>(null)
let editor: monaco.editor.IStandaloneCodeEditor | null = null

type Lang = 'java' | 'cpp' | 'python'
const STARTER: Record<Lang, string> = {
  java: `class Solution {
    public int solve(int x, int y) {
        return x + y;
    }
}
`,
  cpp: `#include <bits/stdc++.h>
using namespace std;
class Solution {
public:
    int solve(int x, int y) { return x + y; }
};
`,
  python: `class Solution:
    def solve(self, x: int, y: int) -> int:
        return x + y
`,
}

async function loadProblem() {
  const id = Number(route.params.id)
  try {
    const detail = await problemStore.fetchDetail(id)
    problem.value = detail
    // 后端没数据时给个兜底，方便前端独立演示
    if (!detail || !detail.problem) {
      problem.value = {
        problem: {
          id,
          title: `题目 #${id}`,
          difficulty: 'easy',
          description: '请在此处实现题目要求的方法。',
          inputDesc: '第一行两个整数 x, y',
          outputDesc: '输出 x + y 的结果',
          sampleInput: '2 3',
          sampleOutput: '5',
        },
        tags: [],
      }
    }
  } catch {
    problem.value = {
      problem: {
        id,
        title: `题目 #${id}`,
        difficulty: 'easy',
        description: '请在此处实现题目要求的方法。',
        inputDesc: '第一行两个整数 x, y',
        outputDesc: '输出 x + y 的结果',
        sampleInput: '2 3',
        sampleOutput: '5',
      },
      tags: [],
    }
  }
}

onMounted(async () => {
  await loadProblem()

  code.value = STARTER[language.value]
  if (editorRef.value) {
    editor = monaco.editor.create(editorRef.value, {
      value: code.value,
      language: language.value,
      theme: 'vs-dark',
      automaticLayout: true,
      fontSize: 14,
      minimap: { enabled: false },
      scrollBeyondLastLine: false,
    })
    editor.onDidChangeModelContent(() => {
      if (editor) code.value = editor.getValue()
    })
  }
})

onBeforeUnmount(() => {
  editor?.dispose()
  // 离开页面时停掉判题轮询，否则 store 是全局单例会一直在后台打 /judge/submit/{id}
  judgeStore.cancel()
})

watch(language, (lang) => {
  if (!lang) return
  code.value = STARTER[lang]
  if (editor) {
    monaco.editor.setModelLanguage(editor.getModel()!, lang)
    editor.setValue(code.value)
  }
})

async function submit() {
  if (!auth.isLogin) {
    router.push({ path: '/login', query: { redirect: route.fullPath } })
    return
  }
  if (!problem.value) return
  try {
    await judgeStore.submitAndWait({
      problemId: problem.value.problem.id,
      language: language.value,
      submitCode: code.value,
    })
  } catch {
    /* 拦截器已弹错 */
  }
}

async function runSample() {
  // 样例运行：与提交共用同一接口（前/后端目前未区分样例模式，统一走真提交 + 轮询）
  await submit()
}
</script>

<template>
  <div class="split" v-if="problem">
    <!-- 左：题干 -->
    <div class="split__pane">
      <div class="problem-detail">
        <h1>{{ problem.problem.title }}</h1>
        <div style="margin-bottom: 12px">
          <DifficultyBadge :status="problem.problem.difficulty || 'easy'" />
          <span v-for="t in problem.tags" :key="t.id" class="tag-chip" style="margin-left: 8px">
            {{ t.name }}
          </span>
        </div>

        <h3>题目描述</h3>
        <p style="white-space: pre-wrap">{{ problem.problem.description }}</p>

        <template v-if="problem.problem.inputDesc">
          <h3>输入格式</h3>
          <p style="white-space: pre-wrap">{{ problem.problem.inputDesc }}</p>
        </template>

        <template v-if="problem.problem.outputDesc">
          <h3>输出格式</h3>
          <p style="white-space: pre-wrap">{{ problem.problem.outputDesc }}</p>
        </template>

        <template v-if="problem.problem.sampleInput || problem.problem.sampleOutput">
          <h3>样例</h3>
          <pre>输入：{{ problem.problem.sampleInput }}
输出：{{ problem.problem.sampleOutput }}</pre>
        </template>

        </div>
    </div>

    <!-- 右：编辑器 + 结果 -->
    <div class="split__pane split__pane--right">
      <div class="editor-toolbar">
        <CodeOutlined style="color: #ffa116" />
        <a-select v-model:value="language" size="small" style="width: 100px">
          <a-select-option value="java">Java</a-select-option>
          <a-select-option value="cpp">C++</a-select-option>
          <a-select-option value="python">Python</a-select-option>
        </a-select>
        <span class="spacer" />
        <a-button size="small" @click="runSample">
          <PlayCircleOutlined />运行
        </a-button>
        <a-button size="small" type="primary" @click="submit">
          <SendOutlined />提交
        </a-button>
      </div>

      <div ref="editorRef" class="editor"></div>

      <div class="result-area">
        <template v-if="!judgeStore.current">
          <div style="color: #8c8c8c">运行或提交后，结果会显示在这里</div>
        </template>
        <template v-else>
          <div class="result-line">
            <span class="result-line__label">结果</span>
            <span>
              <StatusPill :status="judgeStore.current.status" />
            </span>
          </div>
          <div class="result-line">
            <span class="result-line__label">得分</span>
            <span class="result-line__value--ok">{{ judgeStore.current.totalScore ?? 0 }}</span>
          </div>
          <div class="result-line">
            <span class="result-line__label">时间</span>
            <span>{{ judgeStore.current.timeUsedMs ?? '-' }} ms</span>
          </div>
          <div class="result-line">
            <span class="result-line__label">内存</span>
            <span>{{ ((judgeStore.current.memoryUsedKb ?? 0) / 1024).toFixed(2) }} MB</span>
          </div>
          <div v-if="judgeStore.current.errorMessage" class="result-line">
            <span class="result-line__label">信息</span>
            <span class="result-line__value--err">{{ judgeStore.current.errorMessage }}</span>
          </div>

          <h4 style="margin: 12px 0 4px; color: #fff; font-size: 13px">用例明细</h4>
          <div v-for="c in judgeStore.cases" :key="c.index" class="result-line">
            <span class="result-line__label">#{{ c.index }}</span>
            <StatusPill :status="c.status" />
            <span style="color: #8c8c8c">{{ c.timeUsedMs ?? 0 }}ms / {{ ((c.memoryUsedKb || 0) / 1024).toFixed(2) }}MB</span>
            <span v-if="c.score !== undefined" style="color: #8c8c8c">得分 {{ c.score }}</span>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
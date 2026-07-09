<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ThunderboltOutlined, BookOutlined } from '@ant-design/icons-vue'
import { useProblemStore } from '@/stores/problem'
import { onMounted } from 'vue'
import DifficultyBadge from '@/components/DifficultyBadge.vue'

const router = useRouter()
const problemStore = useProblemStore()

onMounted(async () => {
  try {
    await problemStore.fetchList({ page: 1, size: 5 })
  } catch {
    /* 静默，列表保持空 */
  }
})
</script>

<template>
  <section class="hero">
    <h1>Fish OJ</h1>
    <p>专注算法训练与面试准备的高质量在线判题平台</p>
    <div class="hero__actions">
      <a-button type="primary" size="large" @click="router.push('/problems')">
        <BookOutlined />浏览题库
      </a-button>
      <a-button size="large" @click="router.push('/problems?difficulty=easy')">
        <ThunderboltOutlined />每日一题
      </a-button>
    </div>
  </section>

  <main class="page">
    <h2 class="page-title">热门题目</h2>
    <div class="card">
      <div v-if="problemStore.list.length === 0" style="padding: 32px; text-align: center; color: #8c8c8c">
        暂无题目，请先在后端初始化题库数据
      </div>
      <div
        v-for="p in problemStore.list"
        :key="p.id"
        class="problem-row"
        @click="router.push(`/problems/${p.id}`)"
      >
        <DifficultyBadge :status="p.difficulty" />
        <div class="problem-row__title">
          <router-link :to="`/problems/${p.id}`">{{ p.title }}</router-link>
        </div>
        <div>
          <span v-for="t in (p as any).tags || []" :key="t.id" class="tag-chip">{{ t.name }}</span>
        </div>
        <div style="text-align: right">
          <a-button type="link" @click.stop="router.push(`/problems/${p.id}`)">开始答题 →</a-button>
        </div>
      </div>
    </div>

    <div class="app-footer">
      Fish OJ · 在线判题平台 · Powered by Vue 3 + Ant Design Vue
    </div>
  </main>
</template>
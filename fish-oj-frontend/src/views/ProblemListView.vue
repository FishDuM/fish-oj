<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useProblemStore, type Difficulty } from '@/stores/problem'
import DifficultyBadge from '@/components/DifficultyBadge.vue'

const router = useRouter()
const problemStore = useProblemStore()

const page = ref(1)
const size = ref(20)
const tagId = ref<number | undefined>()
const difficulty = ref<Difficulty | undefined>()

async function load() {
  try {
    await problemStore.fetchList({
      page: page.value,
      size: size.value,
      tagId: tagId.value,
      difficulty: difficulty.value,
    })
  } catch {
    // 拦截器已弹错；列表保持空态，由 Empty 组件展示
  }
}

onMounted(async () => {
  try {
    await problemStore.fetchTags()
  } catch {
    /* ignore */
  }
  await load()
})

function changePage(p: number) {
  page.value = p
  load()
}

function resetFilter() {
  tagId.value = undefined
  difficulty.value = undefined
  page.value = 1
  load()
}
</script>

<template>
  <main class="page">
    <h2 class="page-title">题库</h2>

    <div class="filter-bar">
      <a-radio-group v-model:value="difficulty" @change="load" button-style="solid">
        <a-radio-button :value="undefined">全部</a-radio-button>
        <a-radio-button value="easy">简单</a-radio-button>
        <a-radio-button value="medium">中等</a-radio-button>
        <a-radio-button value="hard">困难</a-radio-button>
      </a-radio-group>

      <a-select
        v-model:value="tagId"
        placeholder="按标签筛选"
        allow-clear
        style="width: 180px"
        @change="load"
      >
        <a-select-option v-for="t in problemStore.tags" :key="t.id" :value="t.id">
          {{ t.name }}
        </a-select-option>
      </a-select>

      <a-button @click="resetFilter">重置</a-button>
    </div>

    <div class="card">
      <div class="problem-row" style="background: #fafbfc; font-weight: 600; color: #595959">
        <div>难度</div>
        <div>标题</div>
        <div>操作</div>
      </div>

      <a-empty v-if="!problemStore.loading && problemStore.list.length === 0" description="暂无题目" />

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
        <div style="text-align: right">
          <a-button type="link" @click.stop="router.push(`/problems/${p.id}`)">作答 →</a-button>
        </div>
      </div>
    </div>

    <a-pagination
      v-if="problemStore.total > 0"
      style="margin-top: 16px; text-align: right"
      :current="page"
      :page-size="size"
      :total="problemStore.total"
      @change="changePage"
    />
  </main>
</template>

<style scoped>
.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  align-items: center;
}
.problem-row {
  grid-template-columns: 80px 1fr 120px;
}
</style>
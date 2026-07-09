import { defineStore } from 'pinia'
import { ref } from 'vue'
import { request } from '@/utils/request'

export type Difficulty = 'easy' | 'medium' | 'hard'

export interface TagVO {
  id: number
  name: string
}

// 列表 VO（与后端 ProblemListVO 对齐：只有 id/title/difficulty/createTime）
export interface ProblemListVO {
  id: number
  title: string
  difficulty: Difficulty | string
  createTime?: string
}

// 详情内层 VO（与后端 ProblemVO 对齐）
export interface ProblemVO {
  id: number
  title: string
  description?: string
  // 后端字段是 inputDesc / outputDesc / sampleInput / sampleOutput
  inputDesc?: string
  outputDesc?: string
  sampleInput?: string
  sampleOutput?: string
  difficulty?: string
  timeLimitMs?: number
  memoryLimitKb?: number
  status?: number
  createTime?: string
}

// 详情 VO：problem + tags 嵌套结构（与后端 ProblemDetailVO 对齐）
export interface ProblemDetailVO {
  problem: ProblemVO
  tags: TagVO[]
}

// MyBatis-Plus IPage JSON 形状
export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export const useProblemStore = defineStore('problem', () => {
  const list = ref<ProblemListVO[]>([])
  const total = ref(0)
  const tags = ref<TagVO[]>([])
  const loading = ref(false)

  async function fetchList(params: { page?: number; size?: number; tagId?: number; difficulty?: Difficulty }) {
    loading.value = true
    try {
      const data = await request<PageResult<ProblemListVO>>({
        url: '/problem/list',
        method: 'GET',
        params: {
          page: params.page ?? 1,
          size: params.size ?? 20,
          tagId: params.tagId,
          difficulty: params.difficulty,
        },
      })
      list.value = (data && data.records) || []
      total.value = (data && data.total) || 0
    } finally {
      loading.value = false
    }
  }

  async function fetchDetail(id: number) {
    return await request<ProblemDetailVO>({
      url: `/problem/${id}`,
      method: 'GET',
    })
  }

  async function fetchTags() {
    const data = await request<TagVO[]>({ url: '/tag/list', method: 'GET' })
    tags.value = data || []
  }

  return { list, total, tags, loading, fetchList, fetchDetail, fetchTags }
})
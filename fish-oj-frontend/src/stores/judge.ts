import { defineStore } from 'pinia'
import { ref } from 'vue'
import http, { request, type ApiResult } from '@/utils/request'

// 与后端 SubmitStatus 枚举值保持一致（pending / judging / accepted / wrong_answer / ...）
export type JudgeStatus =
  | 'pending'
  | 'judging'
  | 'accepted'
  | 'wrong_answer'
  | 'time_limit_exceeded'
  | 'memory_limit_exceeded'
  | 'compile_error'
  | 'runtime_error'

// 短码 -> 后端原文（用于 fakeJudge 等内部生成假数据时复用）
export type JudgeStatusShort = 'ac' | 'wa' | 'tle' | 'mle' | 're' | 'ce'

export interface SubmitReq {
  problemId: number
  language: string
  // 与后端 SubmitRequest.submitCode 字段名一致
  submitCode: string
}

// 后端 SubmitDetailVO 字段（继承自 SubmitVO + code）
export interface SubmitVO {
  id: number
  userId?: number
  problemId: number
  language: string
  status: JudgeStatus
  totalScore?: number
  timeUsedMs?: number
  memoryUsedKb?: number
  errorMessage?: string
  createTime?: string
}

export interface SubmitDetailVO extends SubmitVO {
  code?: string
}

// 后端 JudgeCaseVO 字段
export interface JudgeCaseVO {
  id?: number
  submitId?: number
  testCaseId?: number
  // 用例粒度的 status；前端用 index+1 展示
  status: JudgeStatus
  timeUsedMs?: number
  memoryUsedKb?: number
  score?: number
}

// 展示层用的"用例明细行"，补一个 index 方便 UI
export interface JudgeCaseRow extends JudgeCaseVO {
  index: number
}

export const useJudgeStore = defineStore('judge', () => {
  const current = ref<SubmitDetailVO | null>(null)
  const cases = ref<JudgeCaseRow[]>([])
  const polling = ref(false)
  // 当前正在轮询的提交 id, 用于 cancel 时让 poll 跳出
  let inFlightSubmissionId: number | null = null

  async function submit(req: SubmitReq) {
    const id = await request<number>({
      url: '/submit',
      method: 'POST',
      data: req,
    })
    return id
  }

  async function getDetail(id: number) {
    const data = await request<SubmitDetailVO>({
      url: `/submit/${id}`,
      method: 'GET',
    })
    current.value = data
    return data
  }

  async function getCases(id: number) {
    const data = await request<JudgeCaseVO[]>({
      url: `/submit/${id}/cases`,
      method: 'GET',
    })
    cases.value = (data || []).map((c, i) => ({ ...c, index: i + 1 }))
    return cases.value
  }

  /** 取消当前轮询（组件卸载时调用，避免离开页面还在打 /submit/{id}） */
  function cancel() {
    inFlightSubmissionId = null
  }

  /** 轮询直到状态不再是 pending/judging，最多 maxAttempts 次（兜底防后端一直 pending） */
  async function poll(id: number, intervalMs = 1500, maxAttempts = 120) {
    polling.value = true
    inFlightSubmissionId = id
    try {
      for (let i = 0; i < maxAttempts; i++) {
        // cancel() 会把 inFlightSubmissionId 置为 null, 借此跳出循环
        if (inFlightSubmissionId !== id) return null
        const detail = await getDetail(id)
        if (inFlightSubmissionId !== id) return null
        if (detail.status !== 'pending' && detail.status !== 'judging') {
          await getCases(id)
          return detail
        }
        await new Promise((r) => setTimeout(r, intervalMs))
      }
      return null
    } finally {
      if (inFlightSubmissionId === id) {
        polling.value = false
        inFlightSubmissionId = null
      }
    }
  }

  /** 完整提交流程：提交 -> 轮询 -> 返回最终详情 */
  async function submitAndWait(req: SubmitReq) {
    const id = await submit(req)
    const detail = await poll(id)
    return detail
  }

  return { current, cases, polling, submit, getDetail, getCases, poll, cancel, submitAndWait }
})
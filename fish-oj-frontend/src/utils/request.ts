import axios, { type AxiosResponse } from 'axios'
import { message } from 'ant-design-vue'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// 与后端 ErrorCode.SUCCESS 对齐（20000 = 成功）
const SUCCESS_CODE = 20000

const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

http.interceptors.request.use((cfg) => {
  const token = localStorage.getItem('token')
  if (token) cfg.headers.token = token
  return cfg
})

http.interceptors.response.use(
  (resp: AxiosResponse<ApiResult<unknown>>) => {
    const body = resp.data
    if (!body || typeof body !== 'object') return resp
    if (body.code === SUCCESS_CODE) return resp
    // 40100 未登录 / 40101 token 过期 -> 清状态并跳登录
    if (body.code === 40100 || body.code === 40101) {
      useAuthStore().clear()
      const redirect = router.currentRoute.value.fullPath
      router.push({ path: '/login', query: { redirect: encodeURIComponent(redirect) } })
      return Promise.reject(body)
    }
    if (body.code === 40300) {
      message.error('无权限')
      return Promise.reject(body)
    }
    if (body.code === 40400) {
      message.error('资源不存在')
      return Promise.reject(body)
    }
    message.error(body.message || '请求失败')
    return Promise.reject(body)
  },
  (err) => {
    message.error(err?.response?.data?.message || err.message || '网络异常')
    return Promise.reject(err)
  },
)

export async function request<T>(config: Parameters<typeof http.request>[0]): Promise<T> {
  // 调用方可在 config 中传 signal: AbortController.signal, 组件卸载时 abort() 即可取消
  const resp = await http.request<ApiResult<T>>(config)
  return resp.data.data
}

export default http
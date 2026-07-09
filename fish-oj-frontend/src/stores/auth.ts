import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import http, { request, type ApiResult } from '@/utils/request'

export interface LoginReq {
  username: string
  password: string
}

export interface RegisterReq {
  username: string
  password: string
  nickname?: string
  email?: string
}

export interface UserVO {
  id: number
  username: string
  nickname?: string
  email?: string
  role?: string
  avatar?: string
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const user = ref<UserVO | null>(null)

  const isLogin = computed(() => !!token.value)

  function setToken(t: string) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function clear() {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
  }

  async function login(req: LoginReq) {
    const data = await request<{ token: string }>({
      url: '/user/login',
      method: 'POST',
      data: req,
    })
    setToken(data.token)
    await fetchMe()
  }

  async function register(req: RegisterReq) {
    await request<void>({
      url: '/user/register',
      method: 'POST',
      data: req,
    })
  }

  async function fetchMe() {
    if (!token.value) return
    try {
      const data = await request<UserVO>({ url: '/user/me', method: 'GET' })
      user.value = data
    } catch {
      clear()
    }
  }

  async function logout() {
    try {
      await http.post<ApiResult<void>>('/user/logout')
    } catch {
      // 后端 404/401 都忽略——本地清掉 token 即可
    }
    clear()
  }

  return { token, user, isLogin, login, register, logout, fetchMe }
})
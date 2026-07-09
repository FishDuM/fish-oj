<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'

const auth = useAuthStore()
const router = useRouter()
const loading = ref(true)

onMounted(async () => {
  if (!auth.isLogin) {
    router.push('/login')
    return
  }
  try {
    await auth.fetchMe()
    if (!auth.user) {
      message.warning('会话已过期，请重新登录')
      router.push('/login')
    }
  } catch {
    message.warning('会话已过期，请重新登录')
    router.push('/login')
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <main class="page">
    <h2 class="page-title">个人主页</h2>
    <div v-if="loading" style="padding: 48px; text-align: center; color: #8c8c8c">
      加载中...
    </div>
    <div class="card" style="padding: 24px" v-else-if="auth.user">
      <div style="display: flex; gap: 24px; align-items: center">
        <div
          style="
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background: #ffa116;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 32px;
            font-weight: 600;
          "
        >
          {{ auth.user.nickname?.[0] || auth.user.username?.[0] || 'U' }}
        </div>
        <div>
          <div style="font-size: 20px; font-weight: 600">
            {{ auth.user.nickname || auth.user.username }}
          </div>
          <div style="color: #8c8c8c; margin-top: 4px">@{{ auth.user.username }}</div>
          <div v-if="auth.user.email" style="color: #595959; margin-top: 4px">
            {{ auth.user.email }}
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
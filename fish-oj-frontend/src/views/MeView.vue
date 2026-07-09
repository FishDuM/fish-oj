<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

onMounted(async () => {
  if (auth.isLogin) {
    await auth.fetchMe()
  } else {
    router.push('/login')
  }
})
</script>

<template>
  <main class="page">
    <h2 class="page-title">个人主页</h2>
    <div class="card" style="padding: 24px" v-if="auth.user">
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
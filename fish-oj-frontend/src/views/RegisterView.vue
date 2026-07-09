<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { message } from 'ant-design-vue'

const auth = useAuthStore()
const router = useRouter()
const form = reactive({ username: '', password: '', nickname: '', email: '' })
const loading = reactive({ value: false })

async function onSubmit() {
  if (!form.username || !form.password) {
    message.warning('请填写用户名和密码')
    return
  }
  loading.value = true
  try {
    await auth.register(form)
    message.success('注册成功，请登录')
    router.push('/login')
  } catch {
    /* ignore */
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <h2>注册 Fish OJ</h2>
      <a-form layout="vertical">
        <a-form-item label="用户名">
          <a-input v-model:value="form.username" placeholder="字母数字下划线" size="large" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input-password v-model:value="form.password" placeholder="至少 6 位" size="large" />
        </a-form-item>
        <a-form-item label="昵称">
          <a-input v-model:value="form.nickname" placeholder="可选" size="large" />
        </a-form-item>
        <a-form-item label="邮箱">
          <a-input v-model:value="form.email" placeholder="可选" size="large" />
        </a-form-item>
        <a-button
          type="primary"
          size="large"
          :loading="loading.value"
          class="submit"
          @click="onSubmit"
        >
          注册
        </a-button>
      </a-form>

      <div class="auth-switch">
        已有账号？
        <router-link to="/login">直接登录</router-link>
      </div>
    </div>
  </div>
</template>
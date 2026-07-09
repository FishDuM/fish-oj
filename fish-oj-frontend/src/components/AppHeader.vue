<script setup lang="ts">
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  HomeOutlined,
  BookOutlined,
  TrophyOutlined,
  UserOutlined,
  LoginOutlined,
  LogoutOutlined,
} from '@ant-design/icons-vue'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const auth = useAuthStore()
const userMenuOpen = ref(false)

async function handleLogout() {
  await auth.logout()
  userMenuOpen.value = false
  router.push('/')
}
</script>

<template>
  <header class="app-header">
    <router-link to="/" class="app-header__brand">
      <img src="/logo.svg" alt="Fish OJ" />
      <span>Fish OJ</span>
    </router-link>

    <nav class="app-header__nav">
      <router-link to="/" :class="{ active: route.path === '/' }">
        <HomeOutlined style="margin-right: 6px" />首页
      </router-link>
      <router-link to="/problems">
        <BookOutlined style="margin-right: 6px" />题库
      </router-link>
      <router-link to="/ranking">
        <TrophyOutlined style="margin-right: 6px" />排行榜
      </router-link>
    </nav>

    <div class="app-header__user">
      <template v-if="auth.isLogin">
        <a-dropdown v-model:open="userMenuOpen" trigger="click">
          <a class="user-trigger">
            <UserOutlined style="margin-right: 6px" />
            {{ auth.user?.nickname || auth.user?.username || '我' }}
          </a>
          <template #overlay>
            <a-menu>
              <a-menu-item @click="router.push('/me')">个人主页</a-menu-item>
              <a-menu-item @click="handleLogout">
                <LogoutOutlined />退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </template>
      <template v-else>
        <router-link to="/login">
          <LoginOutlined style="margin-right: 6px" />登录
        </router-link>
        <a-button type="primary" size="small" @click="router.push('/register')">注册</a-button>
      </template>
    </div>
  </header>
</template>

<style scoped>
.user-trigger {
  color: rgba(255, 255, 255, 0.85);
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 4px;
}
.user-trigger:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}
.app-header__user a {
  color: rgba(255, 255, 255, 0.85);
  padding: 6px 10px;
  border-radius: 4px;
}
.app-header__user a:hover {
  background: rgba(255, 255, 255, 0.08);
  color: #fff;
}
</style>
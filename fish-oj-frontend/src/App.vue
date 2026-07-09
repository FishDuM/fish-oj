<script setup lang="ts">
import AppHeader from '@/components/AppHeader.vue'
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()

onMounted(() => {
  if (auth.isLogin) auth.fetchMe()
})
</script>

<template>
  <AppHeader />
  <router-view />
</template>

<style scoped></style>
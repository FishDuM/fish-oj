<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  status: 'easy' | 'medium' | 'hard' | string
}>()

const label = computed(() => {
  switch (props.status) {
    case 'easy':
      return '简单'
    case 'medium':
      return '中等'
    case 'hard':
      return '困难'
    default:
      return props.status
  }
})
</script>

<template>
  <span :class="['diff', `diff--${status}`]">{{ label }}</span>
</template>
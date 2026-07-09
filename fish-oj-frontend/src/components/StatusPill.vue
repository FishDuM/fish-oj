<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  status: string
}>()

// 后端 SubmitStatus 枚举值：pending / judging / accepted / wrong_answer /
// time_limit_exceeded / memory_limit_exceeded / compile_error / runtime_error
const cls = computed(() => {
  const s = props.status?.toLowerCase()
  if (s === 'ac' || s === 'accepted') return 'status status--ac'
  if (s === 'wa' || s === 'wrong_answer') return 'status status--wa'
  if (s === 'tle' || s === 'time_limit_exceeded') return 'status status--tle'
  if (s === 'mle' || s === 'memory_limit_exceeded') return 'status status--mle'
  if (s === 're' || s === 'runtime_error') return 'status status--re'
  if (s === 'ce' || s === 'compile_error') return 'status status--ce'
  return 'status status--pending'
})

const text = computed(() => {
  switch (props.status) {
    case 'ac':
    case 'accepted':
      return '通过'
    case 'wa':
    case 'wrong_answer':
      return '错误'
    case 'tle':
    case 'time_limit_exceeded':
      return '超时'
    case 'mle':
    case 'memory_limit_exceeded':
      return '超内存'
    case 're':
    case 'runtime_error':
      return '运行时错误'
    case 'ce':
    case 'compile_error':
      return '编译错误'
    case 'pending':
      return '等待'
    case 'judging':
    case 'running':
      return '运行'
    default:
      return props.status || '未知'
  }
})
</script>

<template>
  <span :class="cls">{{ text }}</span>
</template>
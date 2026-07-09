import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'

import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth'
import './styles/global.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

// 跨标签页同步登录态: 其它标签页登出时 (localStorage token 被清空), 当前页也跟着清
const auth = useAuthStore()
window.addEventListener('storage', (e) => {
  if (e.key === 'token' && !e.newValue) {
    auth.clear()
  }
})

app.mount('#app')
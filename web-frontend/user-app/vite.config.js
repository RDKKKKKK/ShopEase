import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import { VantResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    port: 8080,
    proxy: {
      // 代理 /api 的请求，转发到后端服务
      '/api': {
        target: 'http://127.0.0.1:29110', // 你的后端 API 地址
        changeOrigin: true, // 确保请求的 host 头与 target 保持一致
        rewrite: (path) => path.replace(/^\/api/, ''), // 可选：重写请求路径
      }
    }
  },
  plugins: [
    vue(),
    Components({ resolvers: [VantResolver()] })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})

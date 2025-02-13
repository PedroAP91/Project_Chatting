import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginPage from "../pages/LoginPage.vue";
import ChatPage from "../pages/ChatPage.vue";
import RegisterPage from "../pages/RegisterPage.vue";
import RoomSelectionPage from "@/pages/RoomSelectionPage.vue";
import ChatRoom from "@/pages/ChatRoom.vue";

const routes: Array<RouteRecordRaw> = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: 'Login', component: LoginPage },
  { path: '/register', name: 'Register', component: RegisterPage },
  { path: '/rooms', name: 'RoomSelection', component: RoomSelectionPage },
  { path: '/chat/:roomCode', name: 'ChatRoom', component: ChatRoom, meta: { requiresAuth: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Guard global para rutas protegidas
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem("accessToken");
  if (to.meta.requiresAuth && !token) {
    console.warn("Ruta protegida: no se encontró accessToken. Redirigiendo a login.");
    next({ name: "Login" });
  } else {
    next();
  }
});

export default router;

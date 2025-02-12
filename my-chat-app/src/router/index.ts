import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginPage from "../pages/LoginPage.vue";
import ChatPage from "../pages/ChatPage.vue";

const routes: Array<RouteRecordRaw> = [
  { path: "/", redirect: "/login" },
  { path: "/login", name: "Login", component: LoginPage },
  { path: "/chat", name: "Chat", component: ChatPage, meta: { requiresAuth: true } },
  // En el futuro, podrías agregar rutas para registro, salas privadas, etc.
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Guard global que verifica la presencia del token en localStorage
router.beforeEach((to, _from, next) => {
  // Nota: ahora usamos "accessToken", ya que el login lo almacena con esa clave
  const token = localStorage.getItem("accessToken");
  if (to.meta.requiresAuth && !token) {
    console.warn("Ruta protegida: no se encontró accessToken. Redirigiendo a login.");
    next({ name: "Login" });
  } else {
    next();
  }
});

export default router;

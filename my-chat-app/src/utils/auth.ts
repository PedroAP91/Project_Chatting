export async function refreshToken() {
    const refreshToken = localStorage.getItem("refreshToken");
  
    if (!refreshToken) {
      console.error("🚨 No hay refreshToken en localStorage");
      return false;
    }
  
    try {
      const response = await fetch("http://localhost:8080/auth/refresh-token", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${refreshToken}`,
        },
      });
  
      if (!response.ok) {
        console.error("🚨 Error al refrescar token:", response.statusText);
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        return false;
      }
  
      const data = await response.json();
      console.log("🔄 Nuevo accessToken generado:", data.accessToken);
  
      // ✅ Actualizar el accessToken
      localStorage.setItem("accessToken", data.accessToken);
      return true;
    } catch (error) {
      console.error("🚨 Error en refreshToken:", error);
      return false;
    }
  }
  
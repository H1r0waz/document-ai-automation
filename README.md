# Automatización documental

MVP local para procesar facturas PDF. n8n recibe el archivo, Python extrae campos y Java valida y persiste el resultado.

## Ejecutar

1. Copia `.env.example` a `.env` y define una contraseña segura para PostgreSQL.
2. Ejecuta `docker compose up --build`.
3. Abre n8n en `http://localhost:5678`, importa `n8n/factura-webhook.json` y activa el flujo.
4. Abre la aplicación en `http://localhost:3000`, carga un PDF y revisa el historial en pantalla.

El resultado queda disponible en `GET http://localhost:3000/api/documents`.

## Publicar con Vercel, Render y Neon

1. Sube este proyecto a un repositorio privado de GitHub.
2. En Neon, crea una base de datos y toma su cadena de conexión. En Render, importa el repositorio como **Blueprint**: `render.yaml` crea la API, extractor privado y n8n. Completa las variables marcadas como secretas. La API usa la URL JDBC de Neon; n8n usa host, base, usuario y contraseña de la misma instancia en un esquema separado (`n8n`).
3. Cuando Render genere las URLs públicas, define `DOCUMENT_API_URL` en n8n con la URL de la API y configura `N8N_HOST` y `WEBHOOK_URL` con la URL de n8n.
4. Copia `dashboard/config.production.example.js` como `dashboard/config.js`, reemplaza las dos URLs y súbelo al repositorio. Importa el repositorio en Vercel con `dashboard` como **Root Directory**.
5. En Render, define `APP_ORIGIN` con la URL final de Vercel. Importa y activa el flujo `n8n/factura-webhook.json` en n8n.

No publiques la base de datos ni el extractor. Antes de admitir documentos reales, añade autenticación a la interfaz y protege el webhook: por defecto, los webhooks de n8n no exigen identidad de usuario.

La extracción actual usa texto embebido del PDF y reglas simples. PDFs escaneados requieren OCR; un proveedor de IA u OCR puede añadirse únicamente en el servicio `extractor`.

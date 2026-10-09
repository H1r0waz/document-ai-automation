const localDevelopment = ["localhost", "127.0.0.1"].includes(window.location.hostname);

window.DOCUMENT_AI_CONFIG = localDevelopment
  ? {
      apiBaseUrl: "/api",
      workflowBaseUrl: "/workflow"
    }
  : {
      apiBaseUrl: "https://document-ai-automation.onrender.com/api",
      workflowBaseUrl: "https://document-automation-n8n.onrender.com"
    };

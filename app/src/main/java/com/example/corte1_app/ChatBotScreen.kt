package com.example.corte1_app.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceError
import android.webkit.WebChromeClient
import android.webkit.ConsoleMessage
import android.webkit.WebSettings
import android.os.Handler
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.saveable.rememberSaveable

private const val TAG = "BotpressWebView"

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ChatBotScreen(navController: NavController) {
    val context = LocalContext.current
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // Eliminar cualquier dependencia implícita de Google Play Services
    DisposableEffect(Unit) {
        onDispose {
            // Limpiar cualquier posible recurso al salir de la pantalla
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // WebView para el chatbot
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        mediaPlaybackRequiresUserGesture = false
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        allowFileAccess = true
                        allowContentAccess = true
                        setSupportMultipleWindows(true)
                        javaScriptCanOpenWindowsAutomatically = true

                        // Desactivar la caché para evitar problemas de compatibilidad
                        cacheMode = WebSettings.LOAD_NO_CACHE

                        // Deshabilitar la geolocalización para evitar problemas con Google Play Services
                        setGeolocationEnabled(false)
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView, url: String) {
                            super.onPageFinished(view, url)
                            Log.d(TAG, "Página cargada: $url")

                            // Inyectar CSS para garantizar que el chatbot ocupe todo el espacio
                            val fixCss = """
                                javascript:(function() {
                                    try {
                                        // Crear un estilo para asegurar que el webchat ocupe todo el espacio
                                        var style = document.createElement('style');
                                        style.textContent = `
                                            body, html {
                                                margin: 0 !important;
                                                padding: 0 !important;
                                                height: 100% !important;
                                                width: 100% !important;
                                                overflow: hidden !important;
                                            }
                                            #webchat {
                                                position: fixed !important;
                                                top: 0 !important;
                                                left: 0 !important;
                                                width: 100% !important;
                                                height: 100% !important;
                                                z-index: 9999 !important;
                                            }
                                            #webchat .bpWebchat {
                                                position: unset !important;
                                                width: 100% !important;
                                                height: 100% !important;
                                                max-height: 100% !important;
                                                max-width: 100% !important;
                                            }
                                            #webchat .bpFab {
                                                display: none !important;
                                            }
                                            .bp-widget-widget {
                                                height: 100% !important;
                                            }
                                            .bpw-layout {
                                                width: 100% !important;
                                                height: 100% !important;
                                            }
                                            .bpw-chat-container {
                                                height: calc(100% - 60px) !important;
                                            }
                                            .bpw-composer {
                                                position: relative !important;
                                                bottom: 0 !important;
                                                width: 100% !important;
                                            }
                                        `;
                                        document.head.appendChild(style);
                                        console.log("Estilos CSS inyectados correctamente");
                                        
                                        // Asegurarse de que el botón de chat esté abierto
                                        if (window.botpress && typeof window.botpress.open === 'function') {
                                            window.botpress.open();
                                            console.log("Botpress chat abierto correctamente");
                                        }
                                        
                                        return true;
                                    } catch (e) {
                                        console.error("Error inyectando CSS: " + e.message);
                                        return false;
                                    }
                                })();
                            """

                            // Ejecutar después de un breve retraso para asegurar que todo esté cargado
                            Handler().postDelayed({
                                try {
                                    view.evaluateJavascript(fixCss) { result ->
                                        Log.d(TAG, "Resultado de inyección CSS: $result")
                                    }
                                    isLoading = false
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error al ejecutar JavaScript: ${e.message}")
                                    errorMessage = "Error al cargar la interfaz: ${e.message}"
                                    isLoading = false
                                }
                            }, 2000)
                        }

                        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                            super.onReceivedError(view, request, error)
                            Log.e(TAG, "Error cargando: ${error.description}")
                            errorMessage = "Error al cargar el chatbot: ${error.description}"
                            isLoading = false
                        }

                        // Asegurarse de que todas las URL se carguen dentro del WebView
                        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                            view.loadUrl(request.url.toString())
                            return true
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                            Log.d(TAG, "Console: ${consoleMessage.message()} [${consoleMessage.lineNumber()}]")
                            return true
                        }
                    }

                    // Cargar HTML personalizado con el chatbot incrustado
                    try {
                        loadDataWithBaseURL(
                            "https://cdn.botpress.cloud/",
                            generateBotpressHTML(),
                            "text/html",
                            "UTF-8",
                            null
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Error cargando HTML: ${e.message}")
                        errorMessage = "Error preparando el chatbot: ${e.message}"
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Indicador de carga simplificado (sin usar CircularProgressIndicator)
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color(0xFF121212)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Cargando LinguaBuddy...",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Por favor espera un momento",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Mostrar mensaje de error si existe
        errorMessage?.let {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Color(0xFF121212))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No se pudo cargar el chatbot",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = Color.Red.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            errorMessage = null
                            isLoading = true
                        }
                    ) {
                        Text("Intentar de nuevo")
                    }
                }
            }
        }
    }
}

// Función para generar el HTML personalizado con el chatbot
private fun generateBotpressHTML(): String {
    return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <title>LinguaBuddy Chat</title>
            <script src="https://cdn.botpress.cloud/webchat/v2.4/inject.js"></script>
            <style>
                body, html {
                    margin: 0;
                    padding: 0;
                    height: 100%;
                    width: 100%;
                    overflow: hidden;
                    background-color: #121212;
                }
                #webchat {
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    z-index: 9999;
                }
                #webchat .bpWebchat {
                    position: unset;
                    width: 100%;
                    height: 100%;
                    max-height: 100%;
                    max-width: 100%;
                }
                #webchat .bpFab {
                    display: none;
                }
            </style>
        </head>
        <body>
            <div id="webchat"></div>
            <script>
                // Declarar variable global para detectar si Botpress fue cargado
                var botpressLoaded = false;
                
                // Esperar a que el webchat esté listo y abrirlo automáticamente
                window.botpress = window.botpress || {};
                window.botpress.on("webchat:ready", function() {
                    console.log("Webchat is ready");
                    botpressLoaded = true;
                    try {
                        window.botpress.open();
                        console.log("Webchat opened successfully");
                    } catch(e) {
                        console.error("Error opening webchat: " + e.message);
                    }
                });
                
                // Inicializar el chatbot con tu configuración
                try {
                    window.botpress.init({
                        "botId": "7bc8300c-c0ab-4bef-9520-4b193b09d01c",
                        "configuration": {
                            "botName": "LinguaBuddy",
                            "botDescription": "I'm here to improve your English conversations.",
                            "website": {},
                            "email": {},
                            "phone": {},
                            "termsOfService": {},
                            "privacyPolicy": {},
                            "color": "#5eb1ef",
                            "variant": "solid",
                            "themeMode": "dark",
                            "fontFamily": "inter",
                            "radius": 1.5,
                            "allowFileUpload": false
                        },
                        "clientId": "7cb475f6-1212-413c-8a54-43e0014bcb3e",
                        "selector": "#webchat"
                    });
                    console.log("Botpress initialized successfully");
                } catch(e) {
                    console.error("Error initializing Botpress: " + e.message);
                }
                
                // Verificar si el chatbot se ha cargado correctamente después de un tiempo
                setTimeout(function() {
                    if (!botpressLoaded) {
                        console.error("Botpress failed to load within expected time");
                    }
                }, 5000);
                
                // Agregar un listener para detectar problemas
                window.onerror = function(message, source, lineno, colno, error) {
                    console.log("Error en JavaScript: " + message + " en " + source + ":" + lineno);
                    return true;
                };
            </script>
        </body>
        </html>
    """.trimIndent()
}
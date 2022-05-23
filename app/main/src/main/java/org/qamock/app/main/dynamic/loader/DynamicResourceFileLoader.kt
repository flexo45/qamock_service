package org.qamock.app.main.dynamic.loader

import com.google.gson.Gson
import org.qamock.app.main.api.json.ResourceObject
import org.qamock.app.main.api.json.ResponseObject
import org.qamock.app.main.service.DynamicResourcesService
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


class DynamicResourceFileLoader(private val resourceService: DynamicResourcesService) {
    val logger = LoggerFactory.getLogger(DynamicResourceFileLoader::class.java)
    private val gson = Gson()
    fun loadScriptsOnInit() {
        load()
    }
    fun loadScriptsManual() {
        load().let {
            if (it.isNotEmpty()) {
                throw RuntimeException(buildString {
                    appendLine("Error on load resources")
                    it.forEach { r ->
                        appendLine("On load ${r.key} exception ${r.value}")
                    }
                })
            }
        }
    }
    private fun load(): Map<String, Exception> {
        val scriptPath = Paths.get("scripts").toAbsolutePath()
        val errors = mutableMapOf<String, Exception>()
        Files.walk(scriptPath)
            .filter { Files.isDirectory(it) && it != scriptPath }
            .forEach { mock ->
                try {
                    loadResource(mock)
                } catch (e: Exception) {
                    errors[mock.toString()] = e
                    logger.error("Error occurred while load resource $mock, $e")
                }
            }
        return errors
    }
    // //---START SCRIPT---//
    // //---END SCRIPT---//
    private fun loadScript(path: String): String {
        val stringBuilder = StringBuilder()
        var write = false
        File(path).forEachLine {
            if(it.contains("//---END SCRIPT---//")) {
                write = false
            }
            if (write) { "${stringBuilder.appendLine(it)}" }
            if (it.contains("//---START SCRIPT---//")) {
                write = true
            }
        }
        return stringBuilder.toString()
    }

    private fun loadResource(mock: Path) {
        val text = File("$mock/settings.json").readText()
        val json = gson.fromJson(text, ResourceScriptSettings::class.java)
        logger.info("Find config ${json.name}")
        var resource = resourceService.getResource(json.path)
        if (resource != null) {
            resourceService.deleteResource(resource.id)
        }
        val resourceObject = ResourceObject().apply {
            path = json.path
            strategy = when(json.dispatch) {
                "sequence" -> 0
                "random" -> 1
                "script" -> 2
                else -> 0 }
            default_resp = -1
            methods = json.methods
            script = if (json.dispatchScript.isNullOrEmpty()) "" else loadScript("$mock/${json.dispatchScript}")
            logging = if (json.logging) 0 else 1
        }
        resourceService.createResource(resourceObject)
        resource = resourceService.getResource(json.path)
        json.responses.forEach { mockResponse ->
            resourceService.createResponse(ResponseObject().apply {
                name = mockResponse.name
                content = mockResponse.body
                code = mockResponse.code
                resource_id = resource.id
                headers = mockResponse.headers
                script = if (mockResponse.prescript.isNullOrEmpty()) "" else loadScript("$mock/${mockResponse.prescript}")
            })
        }
        if (!json.defaultResponse.isNullOrEmpty()) {
            resourceService.updateResource(resourceObject.apply {
                id = resource.id
                default_resp = resourceService
                    .getResponseListOfResource(resource.id)
                    .first { resp -> resp.name == json.defaultResponse }.id
            })
        }
    }
}

data class ResourceScriptSettings(val name: String,
                                  val path: String,
                                  val methods: List<String>,
                                  val logging: Boolean,
                                  val dispatch: String,
                                  val dispatchScript: String?,
                                  val defaultResponse: String?,
                                  val responses: List<ResourceScriptResponseSettings>)

data class ResourceScriptResponseSettings(val name: String,
                                          val code: Int,
                                          val headers: List<String>,
                                          val body: String,
                                          val prescript: String?)
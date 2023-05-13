package cz.upce.fei.testingsubsystem.doc

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.media.Schema

@Operation(summary = "Download a solution file")
@ApiResponses(value = [
    ApiResponse(responseCode = "200", description = "Successful operation",
        content = [Content(mediaType = "application/octet-stream",
            schema = Schema(type = "file", format = "binary")
        )]),
    ApiResponse(responseCode = "404", description = "File not found")])
annotation class DownloadSolutionEndpointV1

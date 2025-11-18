package dev.lysmux.lab4.api.resource;

import dev.lysmux.lab4.api.filter.Secured;
import dev.lysmux.lab4.api.schemas.ApiResponse;
import dev.lysmux.lab4.api.schemas.hit.HitCheckRequest;
import dev.lysmux.lab4.domain.model.HitResult;
import dev.lysmux.lab4.service.hit.HitService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Path("/hits")
@Secured
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class HitResource {
    @Inject
    private HitService hitService;

    @GET
    @Path("{id}")
    public Response getHitById(@PathParam("id") String hitId) {
        HitResult hit = hitService.getHit(hitId);

        return Response.ok()
                .entity(hit)
                .build();
    }

    @GET
    @Path("/list")
    public Response getUserHits(@Context SecurityContext securityContext) {
        List<HitResult> results = hitService.getUserHits(securityContext.getUserPrincipal().getName());

        return Response.ok()
                .entity(results)
                .build();
    }

    @POST
    @Path("/check")
    public Response checkHit(@Valid HitCheckRequest request, @Context SecurityContext securityContext) {
        HitResult hit = hitService.checkHit(
                securityContext.getUserPrincipal().getName(),
                request.x(),
                request.y(),
                request.r()
        );

        return Response.ok()
                .entity(hit)
                .build();
    }

    @POST
    @Path("/clear")
    public Response clearHits(@Context SecurityContext securityContext) {
        hitService.clearUserHits(securityContext.getUserPrincipal().getName());

        return Response.ok()
                .entity(new ApiResponse<Void>("Hits cleared"))
                .build();
    }
}

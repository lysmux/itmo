package dev.lysmux;

import com.fastcgi.FCGIInterface;
import dev.lysmux.fcgi.AppRouter;
import dev.lysmux.fcgi.dto.Request;
import dev.lysmux.fcgi.dto.Response;
import dev.lysmux.server.CoordinatesRouter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AppRouter appRouter = new AppRouter();
        appRouter.includeRouter(new CoordinatesRouter());

//        System.out.println(
//                appRouter.handle(new Request(
//                        "1.1.1.1:80",
//                        "server.com",
//                        HTTPMethod.POST,
//                        "/check",
//                        new HashMap<>(),
//                        "x=1&y=2&r=3",
//                        "{\"x\": 1, \"y\": 2, \"r\": 1}"
//                ))
//        );;

        FCGIInterface fcgiInterface;
        fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            Request request = Request.fromFCGI();

            Response response = appRouter.handle(request);

            System.out.println(response);
        }
    }
}
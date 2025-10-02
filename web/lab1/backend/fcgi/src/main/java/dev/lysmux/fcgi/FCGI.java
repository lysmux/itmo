package dev.lysmux.fcgi;

import com.fastcgi.FCGIInterface;
import dev.lysmux.fcgi.dto.Request;
import dev.lysmux.fcgi.dto.Response;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class FCGI implements Runnable {
    private final AppRouter appRouter;

    public FCGI(AppRouter appRouter) {
        this.appRouter = appRouter;
    }

    public void run() {
        log.info("Starting FCGI server");

        FCGIInterface fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            try {
                Request request = Request.fromFCGI();

                log.info("Received %s request %s from %s".formatted(
                        request.method(),
                        request.path(),
                        request.fromHost()
                ));

                Response response = appRouter.handle(request);
                System.out.println(response);
            } catch (IOException e) {
                log.info("Error while reading request");
            }
        }
    }
}

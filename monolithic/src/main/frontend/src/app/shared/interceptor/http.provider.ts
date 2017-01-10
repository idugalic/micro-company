import { Injector } from '@angular/core';
import { Http, XHRBackend, RequestOptions } from '@angular/http';
import { HttpInterceptor } from './http.interceptor';
import { ErrorHandlerInterceptor } from './errorhandler.interceptor';

import { EventManager } from '../../shared/event-manager.service';

export const customHttpProvider = () => ({
    provide: Http,
    useFactory: (
        backend: XHRBackend,
        defaultOptions: RequestOptions,
        injector: Injector,
        eventManager: EventManager
    ) => new HttpInterceptor(
        backend,
        defaultOptions,
        [
            // Other interceptors can be added here
            new ErrorHandlerInterceptor(eventManager)
        ]
    ),
    deps: [
        XHRBackend,
        RequestOptions,
        Injector,
        EventManager
    ]
});

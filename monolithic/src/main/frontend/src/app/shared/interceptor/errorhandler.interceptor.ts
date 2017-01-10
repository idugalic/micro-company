import { HttpInterceptable } from './http.interceptable';
import { RequestOptionsArgs, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { EventManager } from '../../shared/event-manager.service';

export class ErrorHandlerInterceptor extends HttpInterceptable {

    constructor(private eventManager: EventManager) {
        super();
    }

    requestIntercept(options?: RequestOptionsArgs): RequestOptionsArgs {
        return options;
    }

    responseIntercept(observable: Observable<Response>): Observable<Response> {
        return <Observable<Response>> observable.catch(error => {
            if (!(error.status === 401)) {
                console.error('HTTP Error broadcasted:');
                console.error(error);
                this.eventManager.broadcast( {name: 'microcompanyuiApp.httpError', content: error});
            }
            return Observable.throw(error);
        });
    }
}

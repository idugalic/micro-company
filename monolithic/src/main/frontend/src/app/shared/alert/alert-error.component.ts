import { Component, OnDestroy } from '@angular/core';

import { AlertService } from './alert.service';
import { EventManager } from '../event-manager.service';
import { Subscription } from 'rxjs/Rx';

@Component({
    selector: 'alert-error',
    template: `
        <div class="alerts">
            <div *ngFor="let alert of alerts"  [ngClass]="{\'alert.position\': true, \'toast\': alert.toast}">
                <ngb-alert type="{{alert.type}}" close="alert.close(alerts)"><pre>{{ alert.msg }}</pre></ngb-alert>
            </div>
        </div>`
})
export class AlertErrorComponent implements OnDestroy {

    alerts: any[];
    cleanHttpErrorListener: Subscription;

    constructor(private alertService: AlertService, private eventManager: EventManager) {
        this.alerts = [];
        //More specific error messages can be introduced here. Translations as well.
        this.cleanHttpErrorListener = eventManager.subscribe('microcompanyuiApp.httpError', (response) => {
            this.addErrorAlert(JSON.stringify(response.content));

        });
    }

    ngOnDestroy() {
        if (this.cleanHttpErrorListener !== undefined && this.cleanHttpErrorListener !== null) {
            this.eventManager.destroy(this.cleanHttpErrorListener);
            this.alerts = [];
        }
    }

    addErrorAlert (message, key?, data?) {
        key = key && key !== null ? key : message;
        this.alerts.push(
            this.alertService.addAlert(
                {
                    type: 'danger',
                    msg: key,
                    params: data,
                    timeout: 10000,
                    toast: this.alertService.isToast(),
                    scoped: true
                },
                this.alerts
            )
        );
          console.log(this.alerts);
    }
}

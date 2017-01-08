import { Injectable }     from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { ProjectModel } from './project.model';
import { Observable }     from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class ProjectsService {
  private projectsUrl = '/api/projects';  // URL to web API, should be externalized

  constructor (private http: Http) {}

  private extractListData(res: Response) {
    let body = res.json();
    return body._embedded.projects || { };
  }
  private extractSingleData(res: Response) {
    let body = res.json();
    return body || { };
  }
  private handleError (error: Response | any) {
    // In a real world app, we might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

  public getProjects (): Observable<ProjectModel[]> {
    return this.http.get(this.projectsUrl)
                    .map(this.extractListData)
                    .catch(this.handleError);
  }

  public getProject(id: string): Observable<ProjectModel> {
    const url = `${this.projectsUrl}/${id}`;
    return this.http.get(url)
                  .map(this.extractSingleData)
                  .catch(this.handleError);
  }

  public addProject(project: ProjectModel): Observable<any> {
    const url = `/api/projectcommands`;

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post(url, project , options).catch(this.handleError);
  }


}

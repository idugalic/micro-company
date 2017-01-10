import { Injectable }     from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { ProjectModel } from './project.model';
import { Observable }     from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class ProjectsService {
  private projectsQueryBaseUrl = '/api/projects';
  private projectsCommandBaseUrl = '/api/projectcommands';

  constructor (private http: Http) {}

  private extractListData(res: Response) {
    let body = res.json();
    return body._embedded.projects || { };
  }
  private extractSingleData(res: Response) {
    let body = res.json();
    return body || { };
  }


  public getProjects (): Observable<ProjectModel[]> {
    return this.http.get(this.projectsQueryBaseUrl)
                    .map(this.extractListData);
  }

  public getProject(id: string): Observable<ProjectModel> {
    const url = `${this.projectsQueryBaseUrl}/${id}`;
    return this.http.get(url)
                  .map(this.extractSingleData);
  }

  public addProject(project: ProjectModel): Observable<any> {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post(this.projectsCommandBaseUrl, project , options);
  }


}

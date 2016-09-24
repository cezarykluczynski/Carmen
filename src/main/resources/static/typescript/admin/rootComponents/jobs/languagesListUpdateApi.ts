import {Injectable} from '@angular/core';
import {HttpClient} from '../../util/httpClient';

@Injectable()
export class LanguagesListUpdateApi {

	constructor(private http: HttpClient) {
	}

	public getStatus(): Promise<any> {
		return this.http.get('api/admin/github/job/languages_list_update');
	}

	public run(): Promise<any> {
		return this.http.post('api/admin/github/job/languages_list_update', {});
	}

}

import {Injectable} from 'angular2/core';
import {HttpClient} from '../../util/httpClient';

@Injectable()
export class UsersImportApi {

	constructor(private http: HttpClient) {
	}

	public getStatus(): Promise<any> {
		return this.http.get('api/admin/github/cron/users_import');
	}

}

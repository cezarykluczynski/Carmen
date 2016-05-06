import {Injectable} from '@angular/core';
import {HttpClient} from '../../util/httpClient';

@Injectable()
export class UsersImportApi {

	constructor(private http: HttpClient) {
	}

	public getStatus(): Promise<any> {
		return this.http.get('api/admin/github/cron/users_import');
	}

	public setStatus(status: boolean): Promise<any> {
		return this.http.post('api/admin/github/cron/users_import', {
			enabled: status
		});
	}

}

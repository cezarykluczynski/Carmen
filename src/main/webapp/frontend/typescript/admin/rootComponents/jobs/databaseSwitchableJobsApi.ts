import {Injectable} from '@angular/core';
import {HttpClient} from '../../util/httpClient';
import {DatabaseSwitchableJobDTO} from './dto/databaseSwitchableJobDTO';

@Injectable()
export class DatabaseSwitchableJobsApi {

	constructor(private http: HttpClient) {
	}

	public getAll(): Promise<any> {
		return this.http.get('api/admin/cron/database_switchable_job');
	}

	public updateList(): Promise<any> {
		return this.http.post('api/admin/cron/database_switchable_job/update_list', {});
	}

	public enable(dto: DatabaseSwitchableJobDTO): Promise<any> {
		return this.http.post('api/admin/cron/database_switchable_job/enable', dto);
	}

	public disable(dto: DatabaseSwitchableJobDTO): Promise<any> {
		return this.http.post('api/admin/cron/database_switchable_job/disable', dto);
	}

}

import {Component} from '@angular/core';
import {AbstractStatefulComponent} from '../abstract/abstractStatefulComponent';
import {DatabaseSwitchableJobsApi} from './databaseSwitchableJobsApi';
import {DatabaseSwitchableJobDTO} from './dto/databaseSwitchableJobDTO';
import {NgClass} from '@angular/common';

@Component({
	directives: [NgClass],
	providers: [DatabaseSwitchableJobsApi],
	selector: 'database-switchable-jobs',
	template: `
		<div class="col-md-4">
			<div class="panel panel-default" [class.busy]="isLoading()">
				<div class="panel-heading">Database switchable jobs</div>
				<div class="panel-body">
					<button (click)="updateList()" class="btn btn-primary">Update list</button>
					<hr>
					<ul class="list-group">
						<li class="list-group-item" *ngFor="let job of databaseSwitchableJobsList">
							<small [innerHtml]="job.name" class="database-switchable-jobs__job" title="{{job.name}}"></small>
							<button class="btn btn-primary database-switchable-jobs__status" [ngClass]="getButtonClass(job)"
								[innerHtml]="getStatus(job)" (click)="toggleStatus(job)"></button>
						</li>
					</ul>
				</div>
			</div>
		</div>
	`
})
export class DatabaseSwitchableJobsComponent extends AbstractStatefulComponent {

	public databaseSwitchableJobsList: Array<any>;

	constructor(private databaseSwitchableJobsApi: DatabaseSwitchableJobsApi) {
		super();
		this.reload();
	}

	public reload() {
		this.setLoading(true);
		this.databaseSwitchableJobsApi.getAll().then((response: Array<DatabaseSwitchableJobDTO>) => {
			this.setLoading(false);
			this.sortAlphabeticallyByName(response);
			this.databaseSwitchableJobsList = response;
		});
	}

	public updateList() {
		this.setLoading(true);
		this.databaseSwitchableJobsApi.updateList().then(() => {
			this.reload();
		});
	}

	public getStatus(job: DatabaseSwitchableJobDTO): String {
		return job.enabled ? 'enabled' : 'disabled';
	}

	public getButtonClass(job: DatabaseSwitchableJobDTO): String {
		return job.enabled ? 'btn-success' : 'btn-danger';
	}

	public toggleStatus(job: DatabaseSwitchableJobDTO) {
		this.setLoading(true);
		let promise: Promise<any> = job.enabled ?
			this.databaseSwitchableJobsApi.disable(job) :
			 this.databaseSwitchableJobsApi.enable(job);
		promise.then((response) => {
			this.reload();
		});
	}

	private sortAlphabeticallyByName(unsortedArray: Array<DatabaseSwitchableJobDTO>) {
		unsortedArray.sort((base, compare) => {
			return base.name > compare.name ? 1 :-1;
		});
	}

}

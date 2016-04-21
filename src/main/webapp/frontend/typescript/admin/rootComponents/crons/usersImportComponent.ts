import {Component} from 'angular2/core';
import {UsersImportApi} from './usersImportApi';
import {AbstractStatefulComponent} from '../abstract/abstractStatefulComponent';

@Component({
	providers: [UsersImportApi],
	selector: 'crons-users-import',
	template: `
		<div class="col-md-4">
			<div class="panel panel-default">
				<div class="panel-heading">Users import</div>
				<div class="panel-body">
					<div class="btn-group">
						<button *ngIf="!enabled" (click)="enable()" class="btn btn-success">Enable</button>
						<button *ngIf="enabled" (click)="disable()" class="btn btn-warning">Disable</button>
						<button (click)="refreshStatus()" class="btn btn-primary">Refresh</button>
					</div>
					<hr>
					<span *ngIf="highestGitHubUserId" [innerHtml]="highestGitHubUserId"></span>
					<span *ngIf="!highestGitHubUserId">Didn't run yet</span>
				</div>
			</div>
		</div>
	`
})
export class UsersImportComponent extends AbstractStatefulComponent {

	public highestGitHubUserId: Number;
	public enabled: boolean;
	public running: boolean;

	constructor(private usersImportApi: UsersImportApi) {
		super();
		this.refreshStatus();
	}

	public refreshStatus() {
		let self = this;
		this.setLoading(true);
		this.usersImportApi.getStatus().then((response) => {
			self.highestGitHubUserId = response.highestGitHubUserId;
			self.enabled = response.enabled;
			self.running = response.running;
			self.setLoading(false);
		}).catch(() => {
			self.setLoading(false);
		});
	}

	public enable() {
		let self = this;
		this.setLoading(true);
		this.updateStatus(true).then(response => {
			self.enabled = response.enabled;
			self.setLoading(false);
		}).catch(() => {
			self.setLoading(false);
		});
	}

	public disable() {
		let self = this;
		this.setLoading(true);
		this.updateStatus(false).then(response => {
			self.enabled = response.enabled;
			self.setLoading(false);
		}).catch(() => {
			self.setLoading(false);
		});
	}

	private updateStatus(status: boolean): Promise<any> {
		return this.usersImportApi.setStatus(status);
	}

}

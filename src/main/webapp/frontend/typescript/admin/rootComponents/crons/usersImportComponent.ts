import {Component} from 'angular2/core';
import {UsersImportApi} from './usersImportApi';

@Component({
	providers: [UsersImportApi],
	selector: 'crons-users-import',
	template: `
		<div>
			<span *ngIf="highestGitHubUserId" [innerHtml]="highestGitHubUserId"></span>
			<span *ngIf="!highestGitHubUserId">Didn't run yet</span>

			<span [innerHtml]="enabled"></span>
			<span [innerHtml]="running"></span>

			<div class="btn-group">
				<button *ngIf="!enabled && !loading" class="btn btn-success">Enable</button>
				<button *ngIf="enabled && !loading" class="btn btn-warning">Disable</button>
				<button (click)="refreshStatus()" class="btn btn-primary">Refresh</button>
			</div>
		</div>
	`
})
export class UsersImportComponent {

	public highestGitHubUserId: Number;
	public enabled: boolean;
	public running: boolean;
	public loading: boolean;

	constructor(private usersImportApi: UsersImportApi) {
		this.refreshStatus();
	}

	public refreshStatus(): void {
		this.loading = true;
		this.usersImportApi.getStatus().then((response) => {
			this.highestGitHubUserId = response.highestGitHubUserId;
			this.enabled = response.enabled;
			this.running = response.running;
			this.loading = false;
		});
	}

}

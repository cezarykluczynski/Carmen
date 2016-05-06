import {Component} from '@angular/core';
import {AbstractStatefulComponent} from '../abstract/abstractStatefulComponent';
import {SchemaUpdateApi} from './schemaUpdateApi';
import {NgClass} from '@angular/common';
import {FormatterService} from '../../util/formatterService';

@Component({
	directives: [NgClass],
	providers: [SchemaUpdateApi],
	selector: 'jobs-schema-update',
	template: `
		<div class="col-md-4">
			<div class="panel panel-default" [class.busy]="isLoading()">
				<div class="panel-heading">Schema update</div>
				<div class="panel-body">
					<div class="btn-group">
						<button (click)="run()" class="btn btn-primary btn-warning">Run</button>
						<button (click)="refreshStatus()" class="btn btn-primary">Refresh status</button>
					</div>
					<hr>
					<ul class="list-group">
						<li class="list-group-item list-group-item-info">
							Linguist version: <span [innerHtml]="getLinguistVersion()"></span>
						</li>
						<li class="list-group-item list-group-item-info" [ngClass]="{'list-group-item-warning': isNotSaved()}">
							Changes saved: <span [innerHtml]="isSaved()"></span>
						</li>
					</ul>
				</div>
			</div>
		</div>
	`
})
export class SchemaUpdateComponent extends AbstractStatefulComponent {

	public enabled: boolean;
	public linguistVersion: String;
	public running: boolean;
	public saved: boolean;
	public updated: any;

	constructor(private schemaUpdateApi: SchemaUpdateApi) {
		super();
		this.refreshStatus();
	}

	public refreshStatus() {
		let self = this;
		this.setLoading(true);
		this.schemaUpdateApi.getStatus().then((response) => {
			this.enabled = response.enabled;
			this.linguistVersion = response.linguistVersion;
			this.running = response.running;
			this.saved = response.saved;
			this.updated = response.updated;
			self.setLoading(false);
		}).catch(() => {
			self.setLoading(false);
		});
	}

	public run() {
		let self = this;
		this.setLoading(true);
		this.schemaUpdateApi.run().then((response) => {
			this.enabled = false;
			this.linguistVersion = response.linguistVersion;
			this.running = false;
			this.saved = response.saved;
			this.updated = response.updated;
			self.setLoading(false);
		}).catch(() => {
			self.setLoading(false);
		});
	}

	/* tslint:disable:no-unused-variable */
	private getLinguistVersion(): String {
		return FormatterService.formatGenericOptional(this.linguistVersion);
	}

	private isSaved(): String {
		return FormatterService.booleanToWord(this.saved);
	}

	private isNotSaved(): boolean {
		return typeof this.saved === 'boolean' && !this.saved;
	}

}

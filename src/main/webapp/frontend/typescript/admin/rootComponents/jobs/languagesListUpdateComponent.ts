import {Component} from '@angular/core';
import {AbstractStatefulComponent} from '../abstract/abstractStatefulComponent';
import {LanguagesListUpdateApi} from './languagesListUpdateApi';
import {NgClass} from '@angular/common';
import {FormatterService} from '../../util/formatterService';

@Component({
	directives: [NgClass],
	providers: [LanguagesListUpdateApi],
	selector: 'jobs-languages-list-update',
	template: `
		<div class="col-md-4">
			<div class="panel panel-default" [class.busy]="isLoading()">
				<div class="panel-heading">Languages list update</div>
				<div class="panel-body">
					<div class="btn-group">
						<button (click)="run()" class="btn btn-primary btn-warning">Run</button>
						<button (click)="refreshStatus()" class="btn btn-primary">Refresh status</button>
					</div>
					<hr>
					<ul class="list-group">
						<li class="list-group-item list-group-item-info">
							Persisted languages: <span [innerHtml]="persistedLanguagesCount"></span>
						</li>
						<li class="list-group-item list-group-item-info">
							Linguist languages: <span [innerHtml]="linguistLanguagesCount"></span>
						</li>
						<li class="list-group-item list-group-item-info" [ngClass]="{'list-group-item-warning': updatable}">
							Updatable: <span [innerHtml]="isUpdatable()"></span>
						</li>
					</ul>
				</div>
			</div>
		</div>
	`
})
export class LanguagesListUpdateComponent extends AbstractStatefulComponent {

	public updatable: boolean;
	public persistedLanguagesCount: Number;
	public linguistLanguagesCount: Number;

	constructor(private languagesListUpdateApi: LanguagesListUpdateApi) {
		super();
		this.refreshStatus();
	}

	public refreshStatus() {
		this.updateFromPromise(this.languagesListUpdateApi.getStatus());
	}

	public run() {
		this.updateFromPromise(this.languagesListUpdateApi.run());
	}

	private updateFromPromise(promise: Promise<any>) {
		let self = this;
		this.setLoading(true);
		promise.then((response) => {
			this.updatable = response.updatable;
			this.persistedLanguagesCount = response.persistedLanguagesCount;
			this.linguistLanguagesCount = response.linguistLanguagesCount;
			self.setLoading(false);
		}).catch(() => {
			self.setLoading(false);
		});
	}

	/* tslint:disable:no-unused-variable */
	private isUpdatable(): String {
		return FormatterService.booleanToWord(this.updatable);
	}

}

/// <reference path="../admin.d.ts" />

import {Injectable} from 'angular2/core';
import {Http} from 'angular2/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class HttpClient {
	constructor(private http: Http) {
	}

	get(url): Promise<any> {
		return this.toPromise(this.http.get(this.decorateUrl(url)));
	}

	private toPromise(request: Observable<any>): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			request.subscribe(
				data => resolve(data.json()),
				err => reject(err)
			);
		});
	}

	private decorateUrl(url: string): string {
		return this.getAppBaseUrl() + url;
	}

	private getAppBaseUrl(): string {
		return window.__carmenConfig.appBaseUrl || '';
	}

}

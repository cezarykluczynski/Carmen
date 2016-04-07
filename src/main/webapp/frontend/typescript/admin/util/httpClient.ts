/// <reference path="../admin.d.ts" />

import {Injectable} from 'angular2/core';
import {Http, Headers, RequestOptionsArgs} from 'angular2/http';
import {RequestOptions} from 'angular2/http';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class HttpClient {
	constructor(private http: Http) {
	}

	get(url: string): Promise<any> {
		return this.toPromise(this.http.get(this.decorateUrl(url)));
	}

	post(url: string, params: {}): Promise<any> {
		let headers: Headers = new Headers();
		headers.append('Content-Type', 'application/x-www-form-urlencoded');
		let requestOptions: RequestOptionsArgs = new RequestOptions(headers);
		requestOptions.headers = headers;
		return this.toPromise(this.http.post(this.decorateUrl(url), this.serializeParams(params), requestOptions));
	}

	private toPromise(request: Observable<any>): Promise<any> {
		return new Promise<any>((resolve, reject) => {
			request.subscribe(
				data => resolve(data.json()),
				err => reject(err)
			);
		});
	}

	private serializeParams(params): string {
		return Object.keys(params).reduce((a, k) => {
			a.push(k + '=' + encodeURIComponent(params[k]));
			return a;
		}, []).join('&');
	}

	private decorateUrl(url: string): string {
		return this.getAppBaseUrl() + url;
	}

	private getAppBaseUrl(): string {
		return window.__carmenConfig.appBaseUrl || '';
	}

}

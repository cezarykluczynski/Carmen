import {MockConnection} from '@angular/http/testing/mock_backend';
import {Response, ResponseOptions, RequestMethod} from '@angular/http';
import {MockBackend} from '@angular/http/testing';

export class HttpClientTestHelper {

	public static createResponse(options: {
		connectionBackend: MockBackend,
		url: string,
		asyncResolve?: boolean,
		unsubscribeAfter?: boolean,
		error?: Error,
		method?: RequestMethod
	}, body: any): any {
		let subscription = options.connectionBackend.connections.subscribe((connection: MockConnection) => {
			if (connection.request.url === options.url &&
				(options.method === undefined || options.method === connection.request.method)) {
				if (options.error) {
					this.resolve(options.asyncResolve, () => {
						connection.mockError(options.error);
					});
				} else {
					this.resolve(options.asyncResolve, () => connection.mockRespond(new Response(
						new ResponseOptions({
							body: body
						})
					)));
				}
			} else {
				this.resolve(options.asyncResolve, () => {
					connection.mockError();
				});
			}

			if (options.unsubscribeAfter !== false) {
				subscription.unsubscribe();
			}
		});

		return subscription;
	}

	private static resolve(async: boolean, response: Function): void {
		async === true ? setTimeout(response) : response();
	}

}

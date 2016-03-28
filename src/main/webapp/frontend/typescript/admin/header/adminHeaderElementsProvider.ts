import {Injectable} from 'angular2/core';

@Injectable()
export class AdminHeaderElementsProvider {

	private elements:Array<any>;

	constructor() {
		this.elements = [
			{
				name: 'Servers'
			},
			{
				name: 'Crons'
			}
		];
	}

	public getElements() {
		return this.elements;
	}

}

export class FormatterService {

	public static booleanToWord(param: boolean) {
		return typeof param === 'boolean' ? (param ? 'yes' : 'no') : 'unknown';
	}

	public static formatGenericOptional(param: any) {
		return typeof param === 'undefined' || param === null ? 'unknown' : '' + param;
	}

}

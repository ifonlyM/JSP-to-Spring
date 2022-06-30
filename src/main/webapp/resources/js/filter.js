/**
 * xss 방지 필터 함수 정규식사용
 */
function XSSfilter(str){
	str = str.replace(/\</g, "");
	str = str.replace(/\>/g, "");
	return str;
}
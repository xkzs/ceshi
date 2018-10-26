package cn.sibat.hibernate.spec;

public enum MatchType {
	equal, // filed = value
	notEqual, // field != value
	like, // field like value
	notLike, // field not like value
	
	likeAfter,
	likeBefore,

	// 下面四个用于Number类型的比较
	gt, // filed > value
	ge, // field >= value
	lt, // field < value
	le, // field <= value
	
	isNotNull,
	isNull,

	// 下面四个用于可比较类型(Comparable)的比较
//	greaterThan, // field > value
//	greaterThanOrEqualTo, // field >= value
//	lessThan, // field < value
//	lessThanOrEqualTo, // field <= value
	;

}

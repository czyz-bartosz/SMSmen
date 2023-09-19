const phoneNumberPattern = new RegExp("^\\d{9}$");

function validatePhoneNumber(phoneNumber) {
    if(typeof phoneNumber !== 'string') {
        throw Error('The phone number type should be string!');
    }

    if(!phoneNumberPattern.test(phoneNumber)) {
        throw Error('The phone number should contain 9 digits!');
    }

    return phoneNumber;
}

function validateMessage(message) {
    if(typeof message !== 'string') {
        throw Error('The message should be string!');
    }

    return message;
}

export default class SmsQueueNode {
    constructor(phoneNumber, message) {
        this.phoneNumber = validatePhoneNumber(phoneNumber);
        this.message = validateMessage(message);
    }
}
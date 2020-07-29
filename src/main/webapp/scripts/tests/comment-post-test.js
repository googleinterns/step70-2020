import { postVideoComment } from '/scripts/post-comment.js';
import 'https://apis.google.com/js/api.js';

const VIDEO_ID = 'sample video id';
const COMMENT_TEXT = 'sample comment';

QUnit.module('Post a comment via YouTube API', {
    beforeEach: () => {
        sinon.stub(gapi.client, 'request');
    },
    afterEach: () => { gapi.client.request.restore(); },
});

QUnit.test('Call API with correct request body', function (assert) {
    const done = assert.async();

    gapi.client.request.returns(Promise.resolve(new CustomResponse()));

    postVideoComment(VIDEO_ID, COMMENT_TEXT)
        .then(() => {
            assert.deepEqual(gapi.client.request.getCall(0).args[0],
                createExpectedRequestBody(VIDEO_ID, COMMENT_TEXT));
            done();
        });
});

QUnit.test('Relay the error with correct message', function (assert) {
    const done = assert.async();
    const errorMsg = 'error message';

    gapi.client.request.returns(Promise.reject(new CustomError(errorMsg)));

    postVideoComment(VIDEO_ID, COMMENT_TEXT)
        .catch(error => {
            assert.equal(error.message, errorMsg);
            done();
        });
});

QUnit.test('Successful post returns empty response', function (assert) {
    const done = assert.async();
    assert.expect(0);

    gapi.client.request.returns(Promise.resolve(new CustomResponse()));

    postVideoComment(VIDEO_ID, COMMENT_TEXT)
        .then(() => {
            done();
        });
});


// Helper functions and classes

class CustomResponse extends Response {
    constructor() {
        super();
        this.json = {};
    }
}

class CustomError extends Error {
    constructor(msg) {
        super();
        this.result = { 'error': { 'message': msg, } };
    }
}

function createExpectedRequestBody(videoId, commentText) {
    return {
        'path': '/youtube/v3/commentThreads?part=snippet',
        'method': 'POST',
        'body': {
            'snippet': {
                'videoId': videoId,
                'topLevelComment': {
                    'snippet': {
                        'textOriginal': commentText,
                    },
                },
            },
        },
    };
}
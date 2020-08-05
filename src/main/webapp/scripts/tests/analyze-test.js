const VIDEO_LINK = 'https://www.youtube.com/watch?v=testvideoid';
const SCORE = 0.05;
let fetchStub = null;

QUnit.module('Updating DOM after analyzing video', {
  beforeEach: function() {
    document.getElementById('sentiment-container').innerText = '';
    fetchStub = sinon.stub(window, 'fetch');
  },
  afterEach: function() {
    fetch.restore();
  }
});

QUnit.testStart(() => {
  document.getElementById('video-url').innerText = VIDEO_LINK;
});

QUnit.test('DOM updates with sentiment score', function (assert) {
  const body = { score: SCORE, scoreAvailable: true };
  const blob = new Blob([JSON.stringify(body, null, 2)], {type : 'application/json'});
  const init = { 'status' : 200 , 'statusText' : 'success' };
  const response = new Response(blob, init);

  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container')
        .hasProperty('innerText','5.3');
    done();
  });
});

QUnit.test('DOM updates with error', function (assert) {
  const body = { };
  const blob = new Blob([JSON.stringify(body, null, 2)], {type : 'application/json'});
  const init = { 'status' : 500 , 'statusText' : '>299 error' };
  const response = new Response(blob, init);

  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container').hasProperty('innerText','>299 error');
    done();
  });
});

QUnit.test('DOM updates without score', function (assert) {
  const body = { scoreAvailable: false };
  const blob = new Blob([JSON.stringify(body, null, 2)], {type : 'application/json'});
  const init = { 'status' : 200 , 'statusText' : 'success' };
  const response = new Response(blob, init);

  fetchStub.returns(Promise.resolve(response));

  const done = assert.async()
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container').hasProperty('innerText',
        'Video has no available captions or comments to analyze.');
    done();
  });
});

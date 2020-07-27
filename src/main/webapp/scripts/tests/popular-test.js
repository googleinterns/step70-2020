import popular from '../popular.js';

const mockGetRegionsResponse = {
  items: [
    { snippet: { gl: 'EG', name: 'Example'}}
  ]
}

QUnit.test('region menu display',  function(assert) {
  const done = assert.async();
  const getRegions = sinon.fake.returns(
    Promise.resolve(mockGetRegionsResponse)
  );
  sinon.replace(popular, 'getRegions', getRegions);
  const loadApi = sinon.fake.returns(
    Promise.resolve()
  );
  popular.addRegionOptions().then(() => {
    assert.dom('option').hasText('Example');
    done();
  });
});

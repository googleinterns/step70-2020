QUnit.test('Popular Videos - getPopularRequest', function(assert) {
  const regionCode = 'US';
  const result = getPopularRequest(regionCode);
  assert.equal(result.regionCode, regionCode, 'Expecting value US""');
});

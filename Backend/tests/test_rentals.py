from tests import BaseTestClass
import json

class RentalsTest(BaseTestClass):

    def test_post_rental(self):
        moto_id = 2
        user_id = 2
        request = self.client.post('/rental?moto_id=' + str(moto_id) + '&user_id=' + str(user_id))
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        self.assertEqual(request.status_code, 201)
        self.assertEqual(my_json['rental']['moto_id'], moto_id)
        self.assertEqual(my_json['rental']['user_id'], user_id)
        self.assertTrue(my_json['rental']['active'])

    def test_get_rental(self):
        id = 1
        request = self.client.get('/rental/' + str(id))
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        self.assertEqual(request.status_code, 200)
        self.assertEqual(my_json['rental']['id'], id)

    def test_delete_rental(self):
        id = 1
        request = self.client.delete('/rental/' + str(id))
        self.assertEqual(request.status_code, 200)
        request = self.client.get('/rental/' + str(id))
        self.assertEqual(request.status_code, 404)

    def test_get_all_rentals(self):
        request = self.client.get('/rentals')
        self.assertEqual(request.status_code, 201)



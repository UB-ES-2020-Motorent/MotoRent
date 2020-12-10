from tests import BaseTestClass
import json


class MotosTest(BaseTestClass):

    def test_get_motos(self):
        request = self.client.get('/motos')
        self.assertEqual(request.status_code, 200)

    def test_get_available_motos(self):
        request = self.client.get('/motos?available=true')
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        motos = my_json['motos']
        for moto in motos:
            self.assertTrue(moto['available'])

    def test_get_not_available_motos(self):
        request = self.client.get('/motos?available=false')
        my_json = json.loads(request.data.decode('utf8').replace("'", '"'))
        motos = my_json['motos']
        for moto in motos:
            self.assertFalse(moto['available'])

    def test_delete_moto(self):
        id = 1
        request = self.client.delete('/moto/' + str(id))
        self.assertEqual(request.status_code, 200)
        request = self.client.get('/moto/' + str(id))
        self.assertEqual(request.status_code, 404)





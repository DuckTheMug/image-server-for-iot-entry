import os
import deepface.DeepFace as DeepFace
import flask
import json

# change the working directory to the image folder for image processing
os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), 'images'))
root: str = os.path.dirname(os.path.abspath(__name__))

# line for testing
# os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), '..', '..', '..', 'images'))

app = flask.Flask(__name__)


@app.route('/validate_entry', methods=['POST'])
def validate_entry():
    if flask.request.method == 'POST':
        data = json.loads(flask.request.data.decode('utf-8'))
        img: str = data['img']
        db: str = data['db']

        # check if the params from the server is null
        if (img is None) | (db is None):
            return flask.Response(response='Image or database path is empty.', status=500)
        img = os.path.join(root, img)
        db = os.path.join(root, db)

        # check if the file or db exists
        if (not os.path.exists(img)) | (not os.path.exists(db)):
            return flask.Response(response='Image or database path doesn\'t exist.', status=500)

        result: list = DeepFace.find(img_path=img, db_path=db, silent=True)
        if result.__len__() > 0:
            return flask.Response(response=os.path.relpath(os.path.normpath(str(result[0].values[0][0])), root)
                                  , status=200)
        else:
            return flask.Response(response='No match found.', status=400)


@app.route('/new_user', methods=['POST'])
def validate_new_user():
    if flask.request.method == 'POST':
        data = json.loads(flask.request.data.decode('utf-8'))
        img: str = data['img']

        # check if the params from the server is null
        if img is None:
            return flask.Response(response='Image path is empty.', status=500)
        img = os.path.join(root, img)

        # check if the file exists
        if not os.path.exists(img):
            return flask.Response(response='Image path doesn\'t exist.', status=500)

        face: int = DeepFace.extract_faces(img).__len__()

        # clear cache if exists
        cache: str = os.path.join(os.path.dirname(img), "representations_vgg_face.pkl")
        if os.path.exists(cache):
            os.remove(cache)

        # check if there is no face or more than 1 face
        if (face == 0) | (face >= 2):
            return flask.Response(response='Invalid image.', status=400)

        # using find itself to generate the cache & check if the user has already existed
        if DeepFace.find(os.path.dirname(img), img, silent=True).__len__() > 0:
            return flask.Response(response='User already exists.', status=400)

        return flask.Response(response='Image processed successfully.', status=200)


if __name__ == '__main__':
    app.run()

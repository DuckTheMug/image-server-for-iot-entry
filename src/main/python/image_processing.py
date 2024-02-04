import os
from http import HTTPStatus as status
import deepface.DeepFace as DeepFace
import flask
import json

# line for testing
# os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), '..', '..', '..', 'images'))

# change the working directory to the image folder for image processing
os.chdir(os.path.join(os.path.dirname(os.path.abspath(__name__)), 'images'))

# init deepface model
DeepFace.build_model("VGG-Face")

app = flask.Flask(__name__)


@app.route('/api/validate_entry', methods=['POST'])
def validate_entry():
    if flask.request.headers.get('Content-Type') == 'application/json':
        data = json.loads(flask.request.data.decode('utf-8'))
        img: str = data['img']
        db: str = data['db']

        # check if the params from the server is null
        if (img is None) | (db is None):
            return flask.Response(response='Image or database path is empty.', status=status.INTERNAL_SERVER_ERROR)

        img = os.path.abspath(img)
        db = os.path.abspath(db)

        # check if the file or db exists
        if (not os.path.exists(img)) | (not os.path.exists(db)):
            return flask.Response(response='Image or database path doesn\'t exist.',
                                  status=status.INTERNAL_SERVER_ERROR)

        result: list = DeepFace.find(img_path=img, db_path=db, silent=True)
        if result.__len__() > 0:
            return flask.Response(response=os.path.relpath(os.path.normpath(str(result[0].values[0][0])),
                                                           os.getcwd()), status=status.OK)
        else:
            return flask.Response(response='No match found.', status=status.BAD_REQUEST)
    else:
        return flask.Response(response='Invalid content type.', status=status.UNSUPPORTED_MEDIA_TYPE)


@app.route('/api/new_user', methods=['POST'])
def validate_new_user():
    if flask.request.headers.get('Content-Type') == 'application/json':
        data = json.loads(flask.request.data.decode('utf-8'))
        img: str = data['img']

        # check if the params from the server is null
        if img is None:
            return flask.Response(response='Image path is empty.', status=status.INTERNAL_SERVER_ERROR)
        img = os.path.abspath(img)

        # check if the file exists
        if not os.path.exists(img):
            return flask.Response(response='Image path doesn\'t exist.', status=status.INTERNAL_SERVER_ERROR)

        face: int = DeepFace.extract_faces(img).__len__()

        # clear cache if exists
        cache: str = os.path.join(os.path.dirname(img), "representations_vgg_face.pkl")
        if os.path.exists(cache):
            os.remove(cache)

        # check if there is no face or more than 1 face
        if (face == 0) | (face >= 2):
            return flask.Response(response='Invalid image input.', status=status.BAD_REQUEST)

        # using find itself to generate the cache & check if the user has already existed
        if DeepFace.find(os.path.dirname(img), img, silent=True).__len__() > 0:
            return flask.Response(response='User already exists.', status=status.BAD_REQUEST)

        return flask.Response(response='Image processed successfully.', status=status.OK)
    else:
        return flask.Response(response='Invalid content type.', status=status.UNSUPPORTED_MEDIA_TYPE)


if __name__ == '__main__':
    app.run()
